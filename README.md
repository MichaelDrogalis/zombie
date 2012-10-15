# zombie

A Clojure framework to rapidly spawn data sets. Most of the time during testing, you don't care about the concrete differences among data. Test cases can be simplified by amplifying the differences between data, while at the same time ignoring any similiarities. It's sort of like [QuickCheck](http://www.haskell.org/haskellwiki/Introduction_to_QuickCheck). This is best shown by the examples below.

## Installation

Add the following to your `:dependencies`

    [zombie "0.1.1-SNAPSHOT"]

## Usage

Here's a basic example of data creation with Zombie:

```clojure
(fact
 (let [mike {:age 21}                                       ; mike => {:age 21}
       bill (is-like mike (but-he (has-a-different :age)))] ; bill => {:age <n != 21>}
   (not= (:age mike) (:age bill)))
 => true)
```

Data can be "molded" with the following functions:

    is-like
    but-it
    but-he
    but-she

Data can be manipulated with the following functions:
    
    has-a-different
    has-a-smaller
    has-no
    has-a-nil
    has-a
    has-an-earlier
    has-a-later

Time can be manipulated:

```clojure
(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       owen (is-like mike (but-he (has-an-earlier :birthday)))]
   (time/before? (:birthday owen) (:birthday mike)))
 => true)
```clojure

## Spawning

A `spawn` function allows access to all the declared vars via a var named 'zombies'. Data can then be anonymously named.
`spawn` is made to feel like the `let` form. However, it does not support destructing.

```clojure
(spawn
 {} ; Options, explained below
 [my-expectations {:grade :A}
  _               {:grade :B}
  _               {:grade :C}]
 (fact (count zombies) => 3)
 (fact (apply not= zombies) => true))
```
    
Check out the [tests](https://github.com/MichaelDrogalis/zombie/blob/master/test/zombie/core_test.clj) for more examples.

## Options

On a test by test basis, spawn takes a options map.

### :n

The number of tests to run. The data will be different each time. Defaults to 1.

### :mode

The mode to run `spawn` in. The default mode is `:quiet`. The `:loud` mode writes the data used for each test to the console.

### Example

```clojure
(spawn
 {:n 50 :mode :loud}
 [mike {:age 21}
  owen (is-like mike (but-he (has-a-different :age)))]
 (fact (:age mike) =not=> (:age owen)))
```

This will run 50 facts, where the `:age` of `owen` is different each time, but always obeying the rule that it never equals `(:age mike`).

## Extending the API

For functions `has-no` and `has-a-different`, the API can be extended to dispatch to specific types. Suppose you wanted an implementation of `has-no` to work on a map:

```clojure
(extend-type clojure.lang.PersistentArrayMap
  Differentiate
  (empty-value-for
   [this description attribute]
   (assoc description attribute {})))
```

Then you could do:

```clojure
(fact
 (let [mike {:grades {:math :B :science :A :english :A}}
       some-dude (is-like mike (but-he (has-no :grades)))]
   (empty? (:grade some-dude)))
 => true)
```

## Contribute

Have a function in mind to mold data or extend the API in a good direction? Fork this and pull request, because I probably want it.

## License

Copyright © 2012 Michael Drogalis

Distributed under the Eclipse Public License, the same as Clojure.

## Thanks

Special thanks to [RJMetrics](http://www.rjmetrics.com/) for letting me open-source this project. I developed this library while working there.

