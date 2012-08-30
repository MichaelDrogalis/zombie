# zombie

A Clojure library designed to facilitate quick generation of test case data. Sometimes test cases can be simplified by amplifying the differences between data, while at the same time ignoring any similiarities. This is best shown by the examples below.

## Installation

Add the following to your `:dependencies`

    [zombie "0.0.1"]

## Usage

Here's an example of some typical test data:
    (fact
     (let [pepperoni {:price 9.99 :toppings ["pepperoni"] :size :medium}
           plain     {:price 8.99 :toppings [] :size :medium}]
       (< (:price plain) (:price pepperoni)))
     => true)

Here's what that looks like with Zombie:
    (fact
     (let [pepperoni {:price 9.99 :toppings ["pepperoni"] :size :medium}
           plain     (is-like pepperoni (but-it (has-a-smaller :price)
                                                (has-no :toppings)))]
       (< (:price plain) (:price pepperoni)))
     => true)

## License

Copyright © 2012 Michael Drogalis

Distributed under the Eclipse Public License, the same as Clojure.

