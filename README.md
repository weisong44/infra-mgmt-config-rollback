cc-demo
=======

This demo shows the key concept of config center core, namely

 - Each change to a ZK node is recorded in one table (c)
 - Changes are grouped into change sets (cs), changes in a change set are solely owned by one user
 - A user can have only one active change set, all changes goes to this change set
 - A change set can be promoted to be approved and then published
 - A change can be staged for later use
 - A change submitted for approval can be rejected
 - A published change can be rolled back
 - When a user logs in, there is a few strategies to produce a view for this user
   - Only show objects that are published to ZK
   - Show objects in current change set (editing) on top of published (published)
   - Show all pending change sets (editing, approving, approved) on top of published (published)
   - Show a list of change sets on top of published
   - Note: the order of change sets here matters, it finally determines the visibility of a change

To run the app
 - Extract to a directory
 - You will need to point to a MySQL, see conf/application.conf
 - Run ./activator
 - Type in “run”, which starts the app in dev mode
 - Connect to http://localhost:9000/login
   - Initially, it will ask you to create the DB schema, after that you could play with it
   - First login
   - Then create/modify/delete VMS clusters
   - Then you can switch users
   - Move change sets through states of change sets, and see what is each users’s view 
   - See all ZK nodes

