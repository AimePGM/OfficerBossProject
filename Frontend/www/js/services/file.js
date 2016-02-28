angular.module('starter.controllers')

.service('FileService', function(Upload, $http, $window,BackendPath) {


  this.upload = function (file, docId) {
     
        console.log("File service from controller: uploading file ("+file.size+") to document id: "+docId);
      
        Upload.upload({
            url: BackendPath.fileServicePath+'/upload',
            method: 'POST',
            data: {file: file, documentId: docId}
        }).then(function (resp) {
            
            console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
        }, function (resp) {
            console.log('Error status: ' + resp.status);
            console.log(resp.config.data)
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        });
        

    };

    this.getFileDetail = function(docId){
      
      $http.get(BackendPath.fileServicePath+'/fileDetail?documentId='+docId)
        .success(function(data){
          
        })
        .error(function(data){
          console.log('cannot reach '+BackendPath.fileServicePath)
          console.log(data)
        });
 

    }

    this.download = function(docId){
      var url = BackendPath.fileServicePath+'/download?documentId='+docId;
      console.log(url)
      $window.open(url);
    }

    this.delete = function(docId){
       $http.get(BackendPath.fileServicePath+'/deleteByDocumentId?documentId='+docId)
        .success(function(data){
          
        })
        .error(function(data){
          console.log('cannot reach '+BackendPath.fileServicePath)
          console.log(data)
        });
    }

    this.copy = function(copyFrom, copyTo){
      $http.get(BackendPath.fileServicePath+'/copy?copyFrom='+copyFrom+'&copyTo='+copyTo)
        .success(function(data){
          console.log(data);

        })
        .error(function(data){
          console.log('cannot reach '+BackendPath.fileServicePath)
        });
    }

})