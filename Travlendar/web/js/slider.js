var maxwalk = document.querySelector('input[name="maxwalking"]');

var maxcycl = document.querySelector('input[name="maxcycling"]');


var rangeValue = function(){
  var newValue = maxwalk.value;
  var target = document.querySelector('.valuemaxwalk');
  target.innerHTML = newValue;
}

var rangeValue2 = function(){
  var newValue = maxcycl.value;
  var target = document.querySelector('.valuemaxcycl');
  target.innerHTML = newValue;
}

maxwalk.addEventListener("input", rangeValue);
maxcycl.addEventListener("input", rangeValue2);