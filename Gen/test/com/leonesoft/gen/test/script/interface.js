/* 
 * Example of a user-define processing script
 * 
 * simply adds events to the global object
 *
 */

gen.addEventListener("START", function(event, args) {
    console.log(event.getName());
});


