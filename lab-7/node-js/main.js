async = require("async");

calcTimeDiff = function(startTime, endTime) {
    return Math.floor(((endTime[0]-startTime[0])*1e9 + (endTime[1]-startTime[1]))/1000);
}

var Fork = function(id) {
    this.id = id;
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) { 
    var func = function(fork, delay, cb) {
        setTimeout(function() {
            if (fork.state) {
                func(fork, 2*delay, cb);
            } else {
                fork.state = 1;
                if (cb) cb();
            }
        }, delay);
    }
    startTime = process.hrtime();
    func(this, 1, cb);
}

Fork.prototype.release = function() { 
    this.state = 0;
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var left = forks[f1], right = forks[f2];
    var todo = function(cb) {
        startTime = process.hrtime();
        left.acquire(function() {
        	console.log(id + " left");
            right.acquire(function() {
        		console.log(id + " right");
                endTime = process.hrtime();
                console.log(id + ", " + calcTimeDiff(startTime, endTime) + ", ");
                setTimeout(function() {
                    left.release();
                    right.release();
                    setTimeout(function() {
                        if (cb) cb();
                    }, Math.random()*10);
                }, Math.random()*10);
            });
        });
    }
    async.waterfall(Array(count).fill(todo), function() {
    });
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        measurements = this.measurements;

    var left = forks[f1], right = forks[f2];
    if (id % 2) {
        var first = left, second = right;
    } else {
        var first = right, second = left;
    }
    var todo = function(cb) {
        startTime = process.hrtime();
        first.acquire(function() {
            second.acquire(function() {
                endTime = process.hrtime();
                console.log(id + ", " + calcTimeDiff(startTime, endTime) + ", ");
                setTimeout(function() {
                    first.release();
                    second.release();
                    setTimeout(function() {
                        if (cb) cb();
                    }, Math.random()*10);
                }, Math.random()*10);
            });
        });
    }
    async.waterfall(Array(count).fill(todo), function() {
    });
}

var Conductor = function(N) {
    this.state = N-1;
    return this;
}

Conductor.prototype.acquire = function(cb) { 
    var func = function(conductor, delay, cb) {
        setTimeout(function() {
            if (conductor.state > 0) {
                --conductor.state;
                if (cb) cb();
            } else {
                func(conductor, 2*delay, cb);
            }
        }, delay);
    }
    func(this, 1, cb);
}

Conductor.prototype.release = function() { 
    ++this.state;
}

Philosopher.prototype.startSimultaneous = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        measurements = this.measurements;

    var left = forks[f1], right = forks[f2];
    var startTime, endTime;
    var todo = function(cb) {
        startTime = process.hrtime();
        var func = function(delay, cb) {
            setTimeout(function() {
                if (left.state || right.state) {
                    func(2*delay, cb);
                } else {
                    left.state = right.state = 1;
                    endTime = process.hrtime();
                	console.log(id + ", " + calcTimeDiff(startTime, endTime) + ", ");
                    setTimeout(function() {
                        left.state = right.state = 0;
                        setTimeout(function() {
                            if (cb) cb();
                        }, Math.random()*10);
                    }, Math.random()*10);
                }
            }, delay);
        }
        func(1, cb);
    }
    async.waterfall(Array(count).fill(todo), function() {
    });
}

Philosopher.prototype.startConductor = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        measurements = this.measurements;

    var left = forks[f1], right = forks[f2];
    var todo = function(cb) {
        startTime = process.hrtime();
        conductor.acquire(function() {
            left.acquire(function() {
                right.acquire(function() {
                    endTime = process.hrtime();
                	console.log(id + ", " + calcTimeDiff(startTime, endTime) + ", ");
                    setTimeout(function() {
                        left.release();
                        right.release();
                        conductor.release();
                        setTimeout(function() {
                            if (cb) cb();
                        }, Math.random()*10);
                    }, Math.random()*10);
                });
            });
        });
    }
    async.waterfall(Array(count).fill(todo), function() {
    });
}


var N = 5;
var forks = [];
var philosophers = [];
for (var i = 0; i < N; i++) {
    forks.push(new Fork(i));
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

conductor = new Conductor(N);

for (var i = 0; i < N; i++) {
    // philosophers[i].startNaive(1000); // naive
    // philosophers[i].startAsym(1000); // asymmetric
    // philosophers[i].startSimultaneous(1000); // simultaneous
    philosophers[i].startConductor(1000); // waiter
}

setTimeout(function() {process.exit(1000)}, 5000);
