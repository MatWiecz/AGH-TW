var async = require("async")

function printAsync(s, cb) {
    var delay = Math.floor((Math.random() * 1000) + 500);
    setTimeout(function () {
        console.log(s);
        if (cb) cb();
    }, delay);
}

function task1(cb) {
    printAsync("1", function () {
        task2(cb);
    });
}

function task2(cb) {
    printAsync("2", function () {
        task3(cb);
    });
}

function task3(cb) {
    printAsync("3", cb);
}

// wywolanie sekwencji zadan
// task1(function () {
//     console.log('done!');
// });


/*
** Zadanie:
** Napisz funkcje loop(n), ktora powoduje wykonanie powyzszej
** sekwencji zadan n razy. Czyli: 1 2 3 1 2 3 1 2 3 ... done
**
*/

function loop(n) {
    task1(function (i=n) {
        if (i !== 1)
            loop(i - 1);
        else
            console.log("done");
    });
}

// function loop(n) {
//     var callArray = [];
//     for (var i = 0; i < n; i++)
//         callArray.push(task1);
//     async.waterfall(callArray, function (err, results) {
//         printAsync("done");
//     })
// }

loop(4);