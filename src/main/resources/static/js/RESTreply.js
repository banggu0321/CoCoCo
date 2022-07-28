var replyREST =(
	function(){
		
		var getAllBoard = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
			
		};
		
		var getAllReply = function(obj, callback){
			$.getJSON('/BOARDrest/'+obj,callback);
		};
		
		return{
			getAllBoard: getAllBoard,
			getAllReply: getAllReply
		}
	}
)();