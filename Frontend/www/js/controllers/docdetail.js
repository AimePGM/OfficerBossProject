angular.module('starter.controllers')
.controller('DocumentDetailCtrl', function($scope, $stateParams,$ionicHistory, $http, $window, 
  FileService, DocumentService, FolderService, PublishDocumentService, BackendPath, 
  UserFactory, DocumentFactory, FileFactory, ReviewFactory, FolderFactory) {
  $ionicHistory.nextViewOptions({
    disableBack: true
  });
  $scope.download = function(){
          FileService.download($stateParams.docId);
  }
  $scope.publish = function(docId,docName){
      DocumentService.publish(docId);
      PublishDocumentService.addDocument(docId, docName);
      $window.location.href=('#/app/doc');
  }
  $scope.delete = function(docId){
      FolderService.delete($stateParams.folderId);
      $window.location.href=('#/app/doc');
  }
  $scope.selectVersion = function(docId, docStatus){
    $scope.showVersionSelector = function(){
        return true;
    }
    $scope.hideVersionSelector = function(){
      $scope.showVersionSelector = function(){
          return false;
      }
    }
    $scope.submit = function(versionType){
      console.log(versionType);
      if(docStatus == 'Draft'){
        DocumentService.submit($stateParams.docId,versionType);
        $window.location.href=('#/app/doc');
      }
      else if(docStatus == 'Reject'){
        $http({
        method: 'POST',
        url: BackendPath.documentServicePath+'/newEditDraft',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function(obj) {
          var str = [];
          for(var p in obj)
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            return str.join("&");
          },
          data: {documentName:$scope.doc.documentName, 
            description:$scope.doc.description, 
            documentId: $stateParams.docId
          }
        
          }).success(function(data, status, headers, config) {
            DocumentService.editable($stateParams.docId, false);
            FolderService.addDocument($stateParams.folderId, data.documentId);
            DocumentService.submit(data.documentId,versionType);
            $window.location.href=('#/app/doc');

          }).
          error(function(data, status, headers, config) {
            console.log('cannot reach '+BackendPath.documentServicePath)
          });
      }
    }    
  }

  FolderFactory.getFolder($stateParams.folderId).then(function(resp){
    if(resp.status == 200){
      $scope.folder = resp.data;
      $scope.versions = {};
      var j = 0;
      for (var i = 0; i < $scope.folder.documentList.length; i++) {
        var tempDocId = $scope.folder.documentList[i];
        DocumentFactory.getDocument(tempDocId).then(function(resp){
            if(resp.status == 200){ 
              var temp = {};
              temp.version = resp.data.version;
              temp.docId = tempDocId;
              $scope.versions[j] = temp;
              $scope.versions[j].docId = resp.data.documentId;
              j++;
            }
            else{ console.log('cannot reach '+BackendPath.documentServicePath); } 
          });
      };
    }
    else{
      console.log('cannot reach '+BackendPath.folderServicePath);
    }
  });
    $scope.doc = {};
    DocumentFactory.getDocument($stateParams.docId).then(function(resp){
        if(resp.status == 200){
          $scope.doc = resp.data;
          
          UserFactory.getUser($scope.doc.creator).then(function(resp){
            if(resp.status == 200){ $scope.creator = resp.data; }
            else{ $scope.creator = "Not available"; }
            
          });
          UserFactory.getUser($scope.doc.approver).then(function(resp){
            if(resp.status == 200){ $scope.approver = resp.data; }
            else{ $scope.approver = "Not available"; }
          });
          FileFactory.getFilename($stateParams.docId).then(function(resp){
            if(resp.status == 200){ $scope.filename = resp.data; }
            else{ $scope.filename = "Not available"; }
          });
          ReviewFactory.getReview($stateParams.docId).then(function(resp){
            if(resp.status == 200){ $scope.review = resp.data; }
            else{ $scope.review = "Not available"; }
          });
        }
        else{
          console.log('cannot reach '+BackendPath.documentServicePath)
        }
        
    });

})
