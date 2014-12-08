package controllers;

import java.util.List;

import models.ChangeSet;
import models.Login;
import models.VmsCluster;
import models.ZkNodeOp;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.ContextHolder;
import daos.ChangeSetDao;
import daos.VmsClusterDao;
import daos.ZkNodeOpDao;

public class Application extends Controller {

	static Form<Login> loginForm = Form.form(Login.class);
	static Form<ZkNodeOp> zkNodeOpForm = Form.form(ZkNodeOp.class);
	static Form<ChangeSet> changeSetForm = Form.form(ChangeSet.class);
	static Form<VmsCluster> vmsClusterForm = Form.form(VmsCluster.class);

	public static Result index() {
        return redirect(
            routes.Application.login()
        );
	}

	public static Result login() {
	    return ok(
	        views.html.login.render(loginForm)
	    );
	}

	public static Result authenticate() {
	    Form<Login> filledForm = loginForm.bindFromRequest();
	    if (filledForm.hasErrors()) {
	        return badRequest(views.html.login.render(loginForm));
	    }
	    return switchUser(filledForm.get().username);
	}

	public static Result switchUser(String username) {
        session().clear();
        session("username", username);
        return redirect(
            routes.Application.listVmsClusters()
        );
        
	}

	public static Result zkNodeOps() {
		populateContext();
		return ok(views.html.zknodeop.render(ZkNodeOpDao.all(), zkNodeOpForm));
	}

	public static Result createZkNodeOps() {
		populateContext();
		Form<ZkNodeOp> filledForm = zkNodeOpForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.zknodeop.render(ZkNodeOpDao.all(), filledForm));
		} else {
			ZkNodeOpDao.create(filledForm.get());
			return redirect(routes.Application.zkNodeOps()); 
		}
	}

	public static Result listVmsClusters() {
		return listVmsClustersIncludePendingChangeSet();
	}
	
	public static Result listVmsClustersIncludePublishedOnly() {
		populateContext();
		return ok(
			views.html.vmscluster.render(
				"Published",
				ZkNodeOpDao.findByType(VmsCluster.class, ZkNodeOpDao.ChangeSetStrategy.publishedOnly), 
				vmsClusterForm)
		);
	}

	public static Result listVmsClustersIncludePendingChangeSet() {
		populateContext();
		return ok(
			views.html.vmscluster.render(
				"Pending",
				ZkNodeOpDao.findByType(VmsCluster.class, ZkNodeOpDao.ChangeSetStrategy.includePending), 
				vmsClusterForm)
		);
	}

	public static Result listVmsClustersIncludeActiveChangeSet() {
		populateContext();
		return ok(
			views.html.vmscluster.render(
				"Active",
				ZkNodeOpDao.findByType(VmsCluster.class, ZkNodeOpDao.ChangeSetStrategy.includeActive), 
				vmsClusterForm)
		);
	}

	public static Result addVmsCluster() {
		populateContext();
		Form<VmsCluster> filledForm = vmsClusterForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.vmscluster.render(
				"Pending",
				ZkNodeOpDao.findByType(VmsCluster.class, ZkNodeOpDao.ChangeSetStrategy.includePending), 
				filledForm));
		} else {
			VmsClusterDao.createOrUpdate(filledForm.get());
			return redirect(routes.Application.listVmsClusters()); 
		}
	}

	public static Result deleteVmsCluster(String name) {
		populateContext();
		VmsClusterDao.delete(name);
		return redirect(routes.Application.listVmsClusters()); 
	}
	
	public static Result listChangeSet() {
		populateContext();
		return ok(
			views.html.changeset.render(ChangeSetDao.all(), changeSetForm)
		);
	}

	public static Result modifyChangeSet(String id, ChangeSet.State state) {
		populateContext();
		ChangeSet cs = ChangeSetDao.getById(Long.valueOf(id));
		cs.setState(state);
		if(state == ChangeSet.State.published) {
			cs.setPublishSequence(ChangeSetDao.getMaxSeq() + 1);
		}
		else {
			cs.setPublishSequence(null);
		}
		ChangeSetDao.createOrUpdate(cs);
		return redirect(routes.Application.listChangeSet()); 
	}

	private static void assertChangeSet(String id, ChangeSet.State ... states) {
		ChangeSet cs = ChangeSetDao.getById(Long.valueOf(id));
		for(ChangeSet.State s : states) {
			if(s == cs.getState()) {
				return;
			}
		}
		throw new RuntimeException("Change set in wrong state " + cs.getState() + ", should be " + states);
	}
	
	public static Result stageChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.editing);
		return modifyChangeSet(id, ChangeSet.State.staged);
	}
	
	public static Result submitChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.editing);
		return modifyChangeSet(id, ChangeSet.State.approving);
	}
	
	public static Result rejectChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.approving);
		return modifyChangeSet(id, ChangeSet.State.rejected);
	}
	
	public static Result approveChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.approving);
		return modifyChangeSet(id, ChangeSet.State.approved);
	}
	
	public static Result publishChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.approved);
		return modifyChangeSet(id, ChangeSet.State.published);
	}
	
	public static Result rollbackChangeSet(String id) {
		assertChangeSet(id, ChangeSet.State.published);
		return modifyChangeSet(id, ChangeSet.State.staged);
	}
	
	public static Result deleteChangeSet(String id) {
		populateContext();
		
		List<ZkNodeOp> list = ZkNodeOpDao.findByChangeSet(Long.valueOf(id));
		for(ZkNodeOp c : list) {
			ZkNodeOpDao.delete(c.getId());
		}
		ChangeSetDao.delete(Long.valueOf(id));
		return redirect(routes.Application.listChangeSet()); 
	}
	
	public static Result editChangeSet(String id) {
		populateContext();
		
		ChangeSet cs = ChangeSetDao.getById(Long.valueOf(id));
		
		ChangeSet curCs = ChangeSetDao.getCurrent(cs.getCreatedBy());
		if(curCs != null) {
			curCs.setState(ChangeSet.State.staged);
			curCs.setPublishSequence(null);
			ChangeSetDao.createOrUpdate(curCs);
		}
		cs.setState(ChangeSet.State.editing);
		cs.setPublishSequence(null);
		ChangeSetDao.createOrUpdate(cs);
		return redirect(routes.Application.listChangeSet()); 
	}
	
	public static void populateContext() {
		if(session().get("username") == null) {
			throw new RuntimeException("Empty session");
		}
		ContextHolder.get().curUser = session().get("username");
	}
}
