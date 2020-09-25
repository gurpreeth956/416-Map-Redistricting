$( document ).ready(function() {
    CurrentMapToggle();
});

$("#current-map").click(function() {
    CurrentMapToggle();
});

$("#generated-map").click(function() {
    GeneratedMapToggle();
});

function CurrentMapToggle() {
    $('#cur-map-li').addClass('active');
    $('#gen-map-li').removeClass('active');
    $('.summary-button').addClass('d-none')
    $('.summary-options').addClass('d-none')
    $('#compare-sidebar-button').addClass('d-none');
    $('#history-sidebar-button').addClass('d-none');
    $('#summary-sidebar-button').addClass('d-none');
    $('#map-pagination').addClass('d-none');
    
    $('.collapse').collapse('hide');
}

function GeneratedMapToggle() {
    $('#cur-map-li').removeClass('active');
    $('#gen-map-li').addClass('active');
    $('.summary-button').removeClass('d-none')
    $('.summary-options').removeClass('d-none')
    $('#compare-sidebar-button').removeClass('d-none');
    $('#history-sidebar-button').removeClass('d-none');
    $('#summary-sidebar-button').removeClass('d-none');
    $('#map-pagination').removeClass('d-none');
    
    $('.collapse').collapse('hide');
}

$('#body-row .collapse').collapse('hide'); 

$('#collapse-icon').addClass('fa-angle-double-left'); 

// Collapse click
$('[data-toggle=sidebar-collapse]').click(function() {
    SidebarCollapse();
});

function SidebarCollapse () {
    $('.menu-collapsed').toggleClass('d-none');
    $('.sidebar-submenu').toggleClass('d-none');
    $('.submenu-icon').toggleClass('d-none');
    $('#sidebar-container').toggleClass('sidebar-expanded sidebar-collapsed');
    $('#sidebar-list').toggleClass('sidebar-list-collapsed');
    
    var SeparatorTitle = $('.sidebar-separator-title');
    if ( SeparatorTitle.hasClass('d-flex') ) {
        SeparatorTitle.removeClass('d-flex');
    } else {
        SeparatorTitle.addClass('d-flex');
    }
    
    $('#collapse-icon').toggleClass('fa-angle-double-left fa-angle-double-right');

    if($('#cur-map-li').hasClass("active")){
        CurrentMapToggle();
    }else{
        GeneratedMapToggle();
    }
}

var slider = document.getElementById("myRange");
var output = document.getElementById("demo");
output.innerHTML = slider.value; // Display the default slider value

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
  output.innerHTML = this.value;
}