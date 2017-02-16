$(document).ready(function() {
   //alert("555");
   //192.168.1.252
   //streamWithAjax("192.168.1.252",2);
   //stream("192.168.1.252",2);
   hideLoading();

});
function setMsgDetail(msg){
    $("#msgDetail").text(msg);
}
function hideLoading(){
    $("#capturePic").attr("src", "images/bg_video.png");
    document.getElementById('loading').style.visibility = 'hidden';

}
function showLoading(){
    $("#capturePic").attr("src", "images/bg_video_blur.png");
    document.getElementById('loading').style.visibility = 'visible';
    setMsgDetail("");
    //document.getElementById('loading').style.color = "Red";
}
function showLoadingOnCommand(){
    $("#capturePic").attr("src", "images/bg_video.png");
    //document.getElementById('loading').style.visibility = 'visible';
    setMsgDetail("");
    //document.getElementById('loading').style.color = "Red";
}
function test(camIp){
  var testUrl = "http://" + camIp + "/?plen=" +
   "undefined";
  $.ajax({
    url: testUrl,
    type: 'GET',
    success: function(data){
        console.log("FFFFFFFFFFFFFFFFFFSuccess: "+data);
    },
    error: function(data) {

        console.log("FFFFFFFFFFFFFFFFFFSuccess If no response, please check the Camera IP Address");
    }
});
}
function stream (camIp,ql){
  showLoading();
  var testUrl = "http://" + camIp + "/?plen=" +
   "undefined" + "&ql=" + ql + "&t=" + Math.random();
  console.log("test url syream: "+testUrl);
  $.get(testUrl, function(result){

    if (result.indexOf("Server is running!") == -1){
      alert("Please input the correct 'Camera IP Address'!");
      setMsgDetail("Please input the correct 'Camera IP Address'!");
    }else{
      console.log("เสรือกออกมานี่");
      console.log(result);
      urlStream = "http://" + camIp + "/stream";
      console.log(urlStream);
      setMsgDetail("");
      $("#capturePic").attr("src", urlStream);
    }
  });
}
function streamWithAjax(camIp,ql){
  showLoading();
  var testUrl = "http://" + camIp + "/?plen=" +
   "undefined" + "&ql=" + ql + "&t=" + Math.random();
  console.log("test url syream: "+testUrl);
  $.ajax({
    url: testUrl,
    type: 'GET',
    success: function(data){
        //console.log("Success");
        hideLoading();
        if (data.indexOf("Server is running!") == -1){
          alert("Please input the correct 'Camera IP Address'!");
          setMsgDetail("Please input the correct 'Camera IP Address'!");
        }else{
          console.log("Server is running!");
          var urlStream = "http://" + camIp + "/stream";
          console.log("strean vedio at: "+urlStream);
          setMsgDetail("");
          $("#capturePic").attr("src", urlStream);
        }
    },
    error: function(data) {
        hideLoading();
        setMsgDetail("No response, Please check your Camera.");
        console.log("If no response, please check the Camera IP Address");
    }
});
}
