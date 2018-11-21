/**
 * 
 */
var $login;
var $signup;
var $shopping;
var $wishlists;

$(document).ready(function() {
	$(".img-responsive").click(imageSelected);
	$("#brand").click(goToItemSelected);
	$('#buyNow').click(buyItem);
	$('#auctionSale').click(doOffer);
});

function buyItem()
{
	console.log("ciao   "+id_item);
$.ajax({
	url:"./buyNow",
	method:"get",
	data:{'id':id_item},
		success:function(){
			console.log("successo");
			
		},error:function(){
			
		}
	
	
});
	
	
}

function doOffer()
{
	
}
function buyNow() {
	$('#auctionSale').addClass('hidden');
}
function auctionSale() {
	$('#buyNow').addClass('hidden');

}

function goToItemSelected() {
	id_item=1;
	document.location.href = "./item_Selected?id_item=" +id_item;
}

function logged() {
	$('#login, #signup').addClass('hidden');

}

function notLogged() {
	$('#wishlist-button,#shopping-button').addClass('hidden');
}

function imageSelected() {
	var c = $(this).attr("src");
	$("#image-selected").attr("src", c);
}

function zoom() {
	console.log("aok")
	$("#image-selected").data('zoom-image', '../images/si1.jpg').elevateZoom({
		responsive : true,
		zoomType : "lens",
		containLensZoom : true
	});
}