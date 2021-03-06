StoneDB
=======
This is a NoSQL Database (Document database).

Structure
=======
StoneDB<br>|<br>|------include<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ core.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ dms.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ dmsRecord.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ msg.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossLatch.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossMmapFile.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|------ ossLatch.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossHash.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossPrimitiveFileOp.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossQueue.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossSocket.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossUtil.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|------ ossLatch.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ixmBucket.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pd.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmd.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdEDU.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ monCB.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdEDUEvent.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdEDUMgr.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdOptions.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ rtn.hpp<br>|------oss<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossSocket.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossPrimitiveFileOp.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ossMmapFile.cpp<br>|------pd<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pd.cpp<br>|------rtn<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ rtn.cpp<br>|------mon<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ monCB.cpp<br>|------msg<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ msg.cpp<br>|------dms<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ dms.cpp<br>|------ixm<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ ixmBucket.cpp<br>|------SQL<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ lexer.l<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ yacc.y<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ Tree.h<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ AST.h<br>|------pmd<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmd.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdEDU.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdEDUMgr.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdAgent.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdMain.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdTCPListener.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ pmdOption.cpp<br>|------client<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ command.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ command.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ commandFactory.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ commandFactory.cpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ edb.hpp<br>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |------ edb.cpp<br>|------Makefile.am<br>|------Makefile.am.Mac.am<br>|------build.sh<br>|------Configure.in<br>|------Configure.in.bak

Milestone
=======

step 1 (Done):

a. install Boost C++ library.

b. use autotools to set up compiling environment.

---files related: Makefile.am, build.sh, configure.in, configure.in.bak.

step 2 (Done):

a. encapsulate all necessary socket functions into ossSocket.

b. coding and compiling source code of server and client.

c. define several basic commands of client, like help, connect and etc.

d. send requests from client to server.

---files related: core.hpp, ossSocket.hpp, ossSocket.cpp, command.cpp, command.hpp, commandFactory.cpp, commandFactory.hpp, edb.cpp, edb.hpp, pmdTcpListener.cpp, pmdAgent.cpp.

step 3 (Done):

a. build lock component to keep multi-threading safety.

b. coding queue structure for threads with mutex and conditional variable.

c. build fundamental file operations component.

d. maintain a kernal controller.

f. initialize configuration file.

e. establish Log component.

---files related: ossLatch.hpp, ossPrimitiveFileOp.hpp, ossPrimitiveFileOp.cpp, pmd.hpp, pmd.cpp, pd.cpp, pd.hpp, pmdMain.cpp, pmdOptions.hpp, pmdOptions.cpp.

step 4 (Done):

a. implement Engine Dispatchable Unit.

b. implement thread scheduling mechanism.

c. implement thread pool.

d. implement lock mechanism on thread queues.

---files related: ossUtil.hpp, pmdEDU.hpp, pmdEDU.cpp, pmdEDUMgr.hpp, pmdEDUMgr.cpp, pmdOptions.hpp, pmdOptions.cpp, edb.cpp, edb.hpp, command.cpp, command.hpp, pmdTCPListener.cpp.

step 5 (Done):

a. complete message encapsulation.

b. client part has been done.

c. define special message protocols for StoneDB.

---files related: msg.hpp, msg.cpp, command.cpp, pmdAgent.cpp. 


step 6 (Done):

establish mapping betweent disk and memory via Mmap.

---files related: ossMmapFile.cpp.


step 7 (Done):

a. BSON and data records structure design.

b. data files design.

---files related: dms.hpp, dms.cpp, dmsRecord.hpp.


step 8 (Done):

a. to finish the file and data operations like insert, delete, query in dms module, etc.

b. to code runtime system connecting database manager and process manager.

---files related: rtn.hpp, rtn.cpp, dms.hpp, dms.cpp, pmd.cpp.

step 9 (Done):

a. implement hash function on keys.

b. achieve automatically establishing index as soon as StoneDB launchs.  

---files related: rtn.hpp, rtn.cpp, ixmBucket.cpp, ixmBucket.hpp, dms.hpp, dms.cpp, ossHash.hpp.

step 10 (Done):

a. understand basic concepts of DB, including transaction, log, ect.

b. define lexical rules of SQL.

c. define grammatical rules of SQL

d. write a SQL Resolver.

---files related: yacc.y, Tree.h, lexer.l, AST.h

step 11 (Done):

finish java driver.

---directory related: driver.

step 12 (Done):

build a snapshot monitor for StoneDB.

---files related: monCB.hpp, monCB.cpp, pmdAgent.cpp, pmd.hpp

step 13 (Done):

a. understand software testing.

b. deploy jenkins.

step 14 (Done):

a. java test app.

b. java monitor app.

------directory related: java, bin.
