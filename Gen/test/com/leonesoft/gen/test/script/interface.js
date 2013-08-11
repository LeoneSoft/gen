/* 
 * Example of a user-define processing script
 * 
 * simply adds events to the global object
 *
 */
// importPackage(Packages.com.leonesoft.gen.data.processor.script);

for(var prop in this) {
    gen.log(prop + " " + typeof this[prop]);
}

gen.addInputDataSource("input1", "jdbc:postgresql://localhost/test?user=gen&password=gen", "SELECT * FROM data");

 // gen.addInputDataSource({name:"input2", dataPath: "path/to/data.dat"});

gen.addEventListener("START", function(event, args) {
    gen.log(event.name());
    return true;
});

gen.addEventListener("NEXT_RECORD", function(event, args) {
    var vars = args.keySet().toArray();
    for(var i = 0; i < vars.length; i++) {
        var k = vars[i];
        gen.log(k.toString());
        gen.log(args.get(k).toString());
        args.put(k, 3);
        gen.log(args.get(k).toString());
    }
    return true;
});

gen.run();


