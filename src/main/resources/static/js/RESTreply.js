var replyREST =(
	function(){
		
		var getAllBoard = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
			
		};
		
		var getAllReply = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
		};
		
		var getAllCategory = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
		}
		
		var getBoardByName = function(obj, callback){
			$.getJSON('/BOARDrest/boardList/'+obj,callback);
		}
		
		return{
			getAllBoard: getAllBoard,
			getAllReply: getAllReply,
			getAllCategory: getAllCategory
		}
	}
)();