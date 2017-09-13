(ns clojure-playground.let-playground)

;; let is a Clojure special form, a fundamental building block of the language.
;;
;; In addition to parameters passed to functions, let provides a way to create
;; lexical bindings of data structures to symbols. The binding, and therefore
;; the ability to resolve the binding, is available only within the lexical
;; context of the let.
;;
;; Clare: "Lexical context" is another way of saying scope, I think.
;; So for instance, you can't use def within a function - you have to use let instead.
;; The scope of the bindings only lasts within the outer brackets of the let statement.
;; Therefore in a function, you can encase the whole function in a let statement,
;; and the bindings will last throughout the function.
;;
;; let uses pairs in a vector for each binding you'd like to make and the value
;; of the let is the value of the last expression to be evaluated. let also
;; allows for destructuring (see destructuring-playground) which is a way to bind symbols to only part of a
;; collection.

;; A basic use for a let:
user=> (let [x 1]
         x)
1

;; Note that the binding for the symbol y won't exist outside of the let:
user=> (let [y 1]
         y)
1
user=> (prn y)
java.lang.Exception: Unable to resolve symbol: y in this context (NO_SOURCE_FILE:7)

;; Another valid use of let:
user=> (let [a 1 b 2]
         (+ a b))
3

;; The forms in the vector can be more complex (this example also uses
;; the thread macro):
user=> (let [c (+ 1 2)
             [d e] [5 6]]
         (-> (+ d e) (- c)))
8

;; The bindings for let need not match up (note the result is a numeric
;; type called a ratio):
user=> (let [[g h] [1 2 3]]
         (/ g h))
1/2

;; ******
;;
;; if-let
;
;; ******
;; !! Warning! Head-melting!
;; (if-let [definition condition] then else):
;; if the value of condition is truthy, then that value is assigned to the definition, and "then" is evaluated
;; otherwise the values is NOT assigned to the definition, and "else" is evaluated

(def x {:whatever 1}) ;; here we create a map that has a value of 1 for the key :whatever
(if-let [value (:whatever x)] value "Not found")
;; now we say that we assign the value of :whatever to the variable called value, but only if there is a value there.
;; Otherwise we return "Not found".
;; In this case we would expect the output to be 1

;; Although you can use this structure with booleans, there's not much point unless you only want to
;; use the resulting boolean if it's true - as evidenced in the first example below.
;; if-let is mostly useful when checking for nil - it's a bit like the ?? operator in C#.

;; In this example if Clare is old, it outputs "Clare is old".
;; (In this example the let part of the statement is rather pointless, as the definition old-clare-age is never used).
(def clare-age 47)
(if-let [old-clare-age (> clare-age 100)] "Clare is old" "clare is not old")

;; In the next two examples, it only outputs Clare's age if it is valid (ie not nil)
(def clare-age nil)
(if-let [valid-clare-age clare-age] (str "Clare has a valid age: " valid-clare-age) "Clare's age is invalid")
;= Clare's age is invalid

(def clare-age 47)
(if-let [valid-clare-age clare-age] (str "Clare has a valid age: " valid-clare-age) "Clare's age is invalid")
;= Clare has a valid age: 47
