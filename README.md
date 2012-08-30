# zombie

A Clojure library designed to facilitate quick generation of test case data. Sometimes test cases can be simplified by amplifying the differences between data, while at the same time ignoring any similiarities. It's sort of like [QuickCheck](http://www.haskell.org/haskellwiki/Introduction_to_QuickCheck). This is best shown by the examples below.

## Installation

Add the following to your `:dependencies`

    [zombie "0.0.1"]

## Usage



Here's a grossly simplified example of data creation with Zombie:

    (fact
     (let [mike {:age 21}
           bill (is-like mike (but-it (has-a-different :age)))]
       (not= (:age mike) (:age bill)))
     => true)

Time can be manipulated:

    (fact
     (let [mike {:birthday (time/date-time 1991 1 13)}
           owen (is-like mike (but-it (has-an-earlier :birthday)))]
       (time/before? (:birthday owen) (:birthday mike)))
     => true)

A `specified-by` function allows access to all the vars via a var named 'all'. Data can then be anonymously named.

    (specified-by
     [my-expectations {:grade :A}
      _               {:grade :B}
      _               {:grade :C}]
     (fact (count all) => 3)
     (fact (apply not= all) => true))
        
Check out the [tests](https://github.com/MichaelDrogalis/zombie/blob/master/test/zombie/core_test.clj) for more examples.

## License

Copyright © 2012 Michael Drogalis

Distributed under the Eclipse Public License, the same as Clojure.

