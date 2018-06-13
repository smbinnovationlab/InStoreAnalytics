var b1Recommand = (function(){
      getRecommendations = function(url, gender, age, mood, visitCount) {
    	 var aProducts;
    	 jQuery.ajax({
              type: "GET",
              async: true,
              url:  url + "/app/Camera/Recommand",
              success: function(data) {
            	  aProducts = data;
              }
          });
    	  
         return aProducts;
      }

      return {
          getRecommendations: getRecommendations
      };
})();