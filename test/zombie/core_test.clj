(ns zombie.core-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as time]
            [zombie.core :refer :all]))

(fact
 (let [mike {:age 21}
       bill (is-like mike (but-it (has-a-different :age)))]
   (not= (:age mike) (:age bill)))
 => true)

(fact
 (let [mike {:favorite-color "green"}
       owen (is-like mike (but-it (has-a-different :favorite-color)))]
   (not= (:favorite-color mike) (:favorite-color owen)))
 => true)

(fact
 (let [mike {:parking-tickets 5}
       owen (is-like mike (but-it (has-no :parking-tickets)))]
   (:parking-tickets owen))
   => 0)

(fact
 (let [mike {:name "Mike"}
       pet-rock (is-like mike (but-it (has-no :name)))]
   (empty? (:name pet-rock)))
 => true)

(fact
 (let [greg {:cats [:boots]}
       mike (is-like greg (but-it (has-no :cats)))]
   (:cats mike))
 => [])

(fact
 (let [bill {:linen-pants-collection [:gray, :blue, :green]}
       mike (is-like bill (but-it (has-a-nil :linen-pants-collection)))]
   (nil? (:linen-pants-collection mike)))
 => true)

(fact
 (let [mike {}
       bill (is-like mike (but-it (has-a :car :red)))]
   (:car bill))
 => :red)

(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       owen (is-like mike (but-it (has-an-earlier :birthday)))]
   (time/before? (:birthday owen) (:birthday mike)))
 => true)

(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       kyle (is-like mike (but-it (has-a-later :birthday)))]
   (time/before? (:birthday mike) (:birthday kyle)))
 => true)

(fact
 (let [mike {:dentist-appointment (time/date-time 2012 8 21)}
       bill (is-like mike (but-it (has-one-day-later :dentist-appointment)))]
   (= (:dentist-appointment bill) (time/date-time 2012 8 22)))
 => true)

(fact
 (let [mike {:dinner-reservations (time/date-time 2012 8 21)}
       owen (is-like mike (but-it (has-one-week-later :dinner-reservations)))]
   (:dinner-reservations owen) => (time/date-time 2012 8 28)))

(specified-by
 [mike {:age 21}
  owen {:age 28}
  bill {:age 30}]
 (fact all => [mike owen bill]))

(specified-by
 [my-expectations {:grade :A}
  _               {:grade :B}
  _               {:grade :C}]
 (fact (count all) => 3)
 (fact (apply not= all) => true))

(fact
 (let [pepperoni {:price 9.99 :toppings ["pepperoni"] :size :medium}
       plain     (is-like pepperoni (but-it (has-a-smaller :price)
                                            (has-no :toppings)))]
   (< (:price plain) (:price pepperoni)))
 => true)

(fact
 (let [mike {:age 21 :favorite-color "green" :favorite-language "Clojure"}
       pete (is-like mike (but-it (has-a-different :favorite-color)
                                  (has-a-different :favorite-language)))]
   (and (not= (:favorite-color mike) (:favorite-color pete))
        (not= (:favorite-language mike) (:favorite-language pete))
        (= (:age mike) (:age pete))))
 => true)

