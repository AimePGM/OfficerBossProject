# Document Service

Document Service|	host	|	Port			
----------------|-----------|--------
Spring			|	localhost	|  8081
Mongo			|	localhost	| 27018

# API


		Path			|	Type	|		Parameter(s)	|	Description
------------------------|-----------|-----------------------|----------
/getalldocuments		|	GET		|						| 	Return all documents
/getdocument			|	GET 	|documentid=String		| 	Return document by {documentid}
/getalldocumentsbyuserid|	GET 	|userid=String			| 	Return document by {userid}
/newdraft	 			|	POST	|documentName=String, description=String, creator=String|	create new draft
/newdocument			|	GET 	|documentName=String, description=String, creator=String|	create new document, status = Waiting for Approval
/save					|	GET 	|documentId=String, documentName=String, description=String| update document, status = current status
/submit					|	GET 	|documentId=String		| submit document, status = Waiting for Approval
/approve				|	GET 	|documentId=String		| approve document, status = Approve
/publish				|	GET 	|documentId=String		| publish document, status = Publish
/reject					|	GET 	|documentId=String		| reject document, status = Reject
/delete					|	GET 	|documentId=String		| delete


# How to run MongoDB

NOTE: ~/data folder will not be able to be uploaded to github because of size limitation, please copy and paste OfficerBossProject to another directory before moving to the next step

run mongo database for this service using command below

```
mongod --dbpath ~/whatever/DocumentService/data --port 27018
```