# zombie

A Clojure framework for declarative semantic data transformation. Sometimes during testing, concrete values don't matter. All that matters is semantics. Capture the essence of your test cases with zombie. See below for usage.

## Installation

Add the following to your `:dependencies`

    [zombie "0.2.1"]

## Usage

I'll motivate the usage by first demonstrating zombie's full power. Later we'll break it down. Let's say I have a function:

```clojure
(defn alcohol-legal [people]
  (filter #(>= (:age %) 21) people))
```

I could write a test such as:

```clojure
(deftest mike-is-legal
  (let [mike {:name "Mike" :age 21}
        pete {:name "Pete" :age 18}]
    (is (= [mike] (alcohol-legal [mike pete])))))
```

But what's the essence of the test? Does Pete's name matter? Does the fact that he's *exactly* 18 matter? Nope. All the matters is that there's someone else that's under 21. Declarative semantic transformations do this better.

```clojure
(deftest mike-is-legal
  (spawn {}
         [mike {:name "Mike" :age 21}
          _    (is-like mike (but-he (has-a-lesser :age)))]
    (is (= [mike] (alcohol-legal zombies)))))
```

A little session at the REPL shows how to leverage Zombie to make new data.

```clojure
user=> (def mike {:nickname "Dro"})
#'user/mike
user=> (def john (is-like mike (but-he (has-a-different :nickname))))
#'user/john
user=> mike
{:nickname "Dro"}
user=> john
{:nickname "{xl#X:YnO%5(YH@A2Zwg)rcM8x@}U|$%F"}
```

```clojure
user=> (def mike {:age 21})
#'user/mike
user=> (def john (is-like mike (but-he (has-a-different :age))))
#'user/john
user=> mike
{:age 21}
user=> john
{:age -138146015N}
```

Data can be "modeled" with the following functions:

    is-like
    but-it
    but-he
    but-she

Data can be manipulated with the following functions:
    
    has-a-different
    has-a-smaller
    has-a-lesser
    has-no
    has-a-nil
    has-a
    has-an-earlier
    has-a-later

Time can be manipulated. Here I use the testing framework [Midje](https://github.com/marick/Midje):

```clojure
(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       owen (is-like mike (but-he (has-an-earlier :birthday)))]
   (time/before? (:birthday owen) (:birthday mike)))
 => true)
```

## Spawning

The `spawn` function allows access to all the declared vars via a var named 'zombies'. Data can then be anonymously named.
`spawn` is made to feel like the `let` form. However, it does not support destructuring.

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

### :aggregate

Configure the name for the aggregate var. Defaults to `zombies`.

### Example

```clojure
(spawn
 {:n 50 :mode :loud :aggregate all-the-vars}
 [mike {:age 21}
  owen (is-like mike (but-he (has-a-different :age)))]
 (fact (:age mike) =not=> (:age owen))
 (fact [mike owen] => all-the-vars))
```

This will run 50 facts, where the `:age` of `owen` is different each time, but always obeying the rule that it never equals `(:age mike`).

Perhaps more clearly at the REPL:

```clojure
user=> (spawn {:n 5 :mode :loud} [mike {:name "Mike"} owen (is-like mike (but-he (has-a-different :name)))])
===================================
Test case  0
===================================
([mike {:name Mike}] [owen {:name c.gwb3pGO<{^!gC}])
===================================
Test case  1
===================================
([mike {:name Mike}] [owen {:name c8NTSxrs9EP&yC5M2#Q[yl7F-C`A}?<XPX:\{\|&&5r=U}])
===================================
Test case  2
===================================
([mike {:name Mike}] [owen {:name UlmmpI5{j'y4C2v<N/L]3I0^*}])
===================================
Test case  3
===================================
([mike {:name Mike}] [owen {:name z0YQy7iv[/nDKGxui}])
===================================
Test case  4
===================================
([mike {:name Mike}] [owen {:name C(l{Ajbvz-`|O}])
nil
```

## Extending the API

For functions such as `has-no` and `has-a-different`, the API can be extended to dispatch to specific types. Suppose you wanted an implementation of `has-no` to work on a map:

```clojure
(extend-type clojure.lang.PersistentArrayMap
  Morph
  (identity-element
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

### To do
- More data manipulation functions (`has-a-greater`, `has-in-range`, `has-any-positive-integer`, etc)
- Exclude Midje from the README. It'll make reading this doc much easier
- Let spawn take no options and use defaults, or read from a config file

## License

Copyright © 2012 Michael Drogalis

Distributed under the Eclipse Public License, the same as Clojure.

## Thanks

Special thanks to [RJMetrics](http://www.rjmetrics.com/) for letting me open-source this project. I developed this library while working there.

