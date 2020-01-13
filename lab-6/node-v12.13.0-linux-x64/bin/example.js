// function printAsync(s, cb) {
//     var delay = Math.floor((Math.random()*1000)+500);
//     setTimeout(function() {
//         console.log(s);
//         if (cb) cb();
//     }, delay);
// }
//
// printAsync("1");
// printAsync("2");
// printAsync("3");
//
// console.log('done!');

function printAsync(s, cb) {
    var delay = Math.floor((Math.random()*1000)+500);
    setTimeout(function() {
        console.log(s);
        if (cb) cb();
    }, delay);
}

printAsync("1", function() {
    printAsync("2", function () {
        printAsync("3", function() {
            console.log('done!');
        });
    });
});
