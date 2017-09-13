(ns clojure-playground.thread-macro-playground)

;; !! As far as I can work out, this is actually nothing to do with threading, as in multi-threading!

;; -> operator
;; aka 'thrush' operator aka 'thread-first' macro
;; also ->> operator aka 'thread-last' macro

;; It's basically a facilitator for fluid syntax

;; Useful articles about this macro:
;;
;; https://blog.nilenso.com/blog/2016/05/12/threading-macros-in-clojure/
;; https://clojure.org/guides/threading_macros
;; http://clojuredocs.org/clojure.core/-%3E

;; Use of `->` (the "thread-first" macro) can help make code
;; more readable by removing nesting. It can be especially
;; useful when using host methods:

;; Arguably a bit cumbersome to read:
user=> (first (.split (.replace (.toUpperCase "a b c d") "A" "X") " "))
"X"

;; Perhaps easier to read:
user=> (-> "a b c d"
           .toUpperCase
           (.replace "A" "X")
           (.split " ")
           first)
"X"

;; So, the formal definition of this in clojuredocs goes like this:
;; (-> x & forms)
;; "Threads the expr through the forms. Inserts x (the expression) as the
;; second item in the first form (by second item, we mean the second item in the brackets, ie the first argument to the function),
;; making a list of it if it is not a list already. If there are more forms, inserts the first form as the
;; second item in second form, etc. So in the example above, the first argument for each "form" (aka function call) is not stated
;; - because it will actually be the result of the line (function call) before."
;; Notice that for functions that only take one argument, you can omit the brackets

;; It can also be useful for pulling values out of deeply-nested
;; data structures:
(def person
            {:name "Mark Volkmann"
             :address {:street "644 Glen Summit"
                       :city "St. Charles"
                       :state "Missouri"
                       :zip 63304}
             :employer {:name "Object Computing, Inc."
                        :address {:street "12140 Woodcrest Dr."
                                  :city "Creve Coeur"
                                  :state "Missouri"
                                  :zip 63141}}})

(-> person :employer :address :city) ;; Return "Creve Coeur"

;; same as above, but with more nesting
(:city (:address (:employer person))) ;; Returns "Creve Coeur"

;; Note that this operator (along with ->>) has at times been
;; referred to as a 'thrush' operator.

;; http://blog.fogus.me/2010/09/28/thrush-in-clojure-redux/

;; ******
;;
;; ->>
;; aka thread-last macro
;; Same as thread-first, except that each expression becomes the LAST param in the following expression (instead of the first)
;;
;; **********

;; To be honest, this is a RUBBISH example as it makes the code harder to read - thread-first would be a better choice here.
;; There is a better example below.
;; Instead of this...
(- 10 (+ 2 (/ 6 3)))
;; ... you can have this:
(->> 3 (/ 6) (+ 2) (- 10))

;; This example is taken from single view code (selected_event.cljs)
(def dob-unformatter "yyyy-mm-dd")
(def dob-formatter "dd MMM yyyy")
(def timestamp "2017-10-10")
(defn clare-unparse
  [formatter target]
   (str formatter " " target)) ;; NOT REAL CODE!!
(defn clare-parse
  [formatter target]
   (str formatter " " target)) ;; NOT REAL CODE!!

;; A better example. Instead of this (see definitions above)...
(clare-unparse dob-formatter (clare-parse dob-unformatter timestamp))
;; ... you can have this (which takes the timestamp, first parses it and then unparses it):
(->> timestamp (clare-parse dob-unformatter) (clare-unparse dob-formatter))

;; the original code (from single view code (selected_event.cljs)) looked like this:
(f/unparse dob-formatter (f/parse dob-unformatter ts))
;; ... and its replacement using ->>:
(->> ts (f/parse dob-unformatter) (f/unparse dob-formatter))



