var replyREST =(
	function(){
		var getBoardAfterSearch = function(obj, callback){
			$.ajax({
				type:'POST',
				url: '/BOARDrest/boardLSearch/',
				data: JSON.stringify(obj),
				dataType:"json",
				contentType: "application/json",
				success:callback
			});
		}
		
		var getReplyAfterDelete = function(obj, callback){
			$.getJSON('/BOARDrest/replyDeleteByID/'+obj,callback);
		}
				
		var getPageNumber = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
		}
		var getBoardPage = function(obj, callback){
			$.getJSON('/BOARDrest/getBoardPage/'+obj,callback);
		}
		
		var getFirstBoardByPageable = function(obj,callback){
			$.getJSON('/BOARDrest/boardList/'+obj,callback);
		}
		
		var getAllBoard = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
			
		};
		
		var getAllReply = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
		};
		
		var getAllCategory = function(obj, callback){
			$.getJSON('/BOARDrest/categoryName/'+obj,callback);
		}
		
		var getBoardByName = function(obj, callback){
			$.getJSON('/BOARDrest/boardList/'+obj,callback);
		}
		
		return{
			getAllBoard: getAllBoard,
			getAllReply: getAllReply,
			getAllCategory: getAllCategory,
			getBoardPage: getBoardPage,
			getFirstBoardByPageable:getFirstBoardByPageable,
			getPageNumber:getPageNumber,
			getReplyAfterDelete:getReplyAfterDelete,
			getBoardAfterSearch:getBoardAfterSearch
		}
	}
)();