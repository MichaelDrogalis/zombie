# zombie

A Clojure framework to rapidly generate data sets. Sometimes you don't care about the concrete differences between data. Test cases can be simplified by amplifying the differences between data, while at the same time ignoring any similiarities. It's sort of like [QuickCheck](http://www.haskell.org/haskellwiki/Introduction_to_QuickCheck). This is best shown by the examples below.

## Installation

Add the following to your `:dependencies`

    [zombie "0.1.1-SNAPSHOT"]

## Usage

Here's a grossly simplified example of data creation with Zombie:

    (fact
     (let [mike {:age 21}
           bill (is-like mike (but-he (has-a-different :age)))]
       (not= (:age mike) (:age bill)))
     => true)

Data can be manipulated with the following methods:
    
    has-a-different
    has-a-smaller
    has-no
    has-a-nil
    has-a
    has-an-earlier
    has-a-later

Time can be manipulated:

    (fact
     (let [mike {:birthday (time/date-time 1991 1 13)}
           owen (is-like mike (but-he (has-an-earlier :birthday)))]
       (time/before? (:birthday owen) (:birthday mike)))
     => true)

A `spawn` function allows access to all the vars via a var named 'zombies'. Data can then be anonymously named.

    (spawn
     [my-expectations {:grade :A}
      _               {:grade :B}
      _               {:grade :C}]
     (fact (count zombies) => 3)
     (fact (apply not= zombies) => true))
        
Check out the [tests](https://github.com/MichaelDrogalis/zombie/blob/master/test/zombie/core_test.clj) for more examples.

## Extending the API

For functions `has-no` and `has-a-different`, the API can be extended to dispatch to specific types. Suppose you wanted an implementation of `has-no` to work on a map:

    (extend-type clojure.lang.PersistentArrayMap
      Differentiate
      (empty-value-for
       [this description attribute]
       (assoc description attribute {}))) 

Then you could do:

    (fact
     (let [mike {:grades {:math :B :science :A :english :A}}
           some-dude (is-like mike (but-he (has-no :grades)))]
       (empty? (:grade some-dude)))
     => true)

## License

Copyright © 2012 Michael Drogalis

Distributed under the Eclipse Public License, the same as Clojure.

## Thanks

Special thanks to [RJMetrics](http://www.rjmetrics.com/) for letting me open-source this project. I developed this library while working there.

