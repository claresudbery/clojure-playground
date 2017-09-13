;; find out what clojure version you have installed.
;; If you are less than 1.7, you won't have the update function.
;; To update the version of Clojure, make your code run in the context of a Leiningen project,
;; then update the version of Clojure used by the Leiningen project.
;; To make your code run in the context of a Leiningen project:
;;   1) Add a project.clj in the same folder as your clojure files
;;   2) Make sure it contains the following 3 lines as bare minimum:
;; (defproject sandbox "0.1.0-SNAPSHOT"
;; :description "A project to run lein repl with a specific clojure version"
;; :dependencies [[org.clojure/clojure "1.7.0-RC1"]])
;;   3) Run 'lein new' from a command prompt in the same folder
;;   4) Restart LightTable
;; Run the following command to check the clojure version:
(clojure-version)

; Define maps with three levels of nesting (the commas are optional):
(def m1 {:a {:b {:c 25}}})
(def m2 {:a 4, :b 5, :c {:d {:e 25}}})

; Print out your map for you to see:
m

; Create a decrement function
(defn dec [input] (- input 1))

; Create an increment function
(defn inc [input] (- input 1))

; Update requires a function – which will receive the previous value
(update m :b dec)

; Define a vector of maps
(def users [{:name "James" :age 26}  {:name "John" :age 43}])

; Define a map of vectors
(def users2 {:user1 ["James" 26], :user2 ["John" 43]})

;; Create a deeply-nested data structure:
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

; Now access one of the deeply nested levels
(-> person :employer :address :city) ;; Gives "Creve Coeur" (using the thread macro ->)
(:city (:address (:employer person))) ;; Gives "Creve Coeur"

;; ' is the shortcut for quote
user> (= 'a (quote a))
;; Output: true
;;
;; quoting keeps something from being evaluated
user> (quote (println "foo"))
;; Output: (println "foo")
;; More here: https://8thlight.com/blog/colin-jones/2012/05/22/quoting-without-confusion.html

;; update-in and update
;; Both of these functions take a function, which is applied to the element of the
;; collection referenced by the first two arguments (the last argument is the function to be applied)
;; update-in, as with other functions, is the same as update, but allows for nested collections.
(def mine {:pet {:age 5 :name "able"}})
(update-in mine [:pet :age] inc)
;; update
(def m {:a 3, :b {:c 25}})
(update m :a inc)

;; IF STATEMENTS
;; If adding 40 to y is still less than 150, then return (+ y 40);
;; otherwise, returns -150.
(def y 40)
(if (< (+ y 40) 150)
  (+ y 40)
  -150)

;; CASE / SWITCH / COND STATEMENTS
;; If adding 40 to y exceeds 150, evaluate the first form.
;;     In this case, it returns -150.
;; If adding 40 to y is less than -150, evaluate the second form.
;;     In this case, it returns 150.
;; If both two predicates return false, evaluate the :else form.
;;     In this case, it returns y plus 40.
(def y 40)
(cond
  (> (+ y 40) 150) -150
  (< (+ y 40) -150) 150
  :else (+ y 40)))


;; #(
;; #( begins the short hand syntax for an inline function definition.
;; The following 2 bits of code are similar:
; anonymous function taking a single argument and printing it
(fn [line] (println line)) ;
; anonymous function taking a single argument and printing it - shorthand
#(println %)


;; MULTIPLE ARITY
;; The function below uses multiple arity. Basically multiple arity allows a function
;; to take a variable number of arguments (so it’s like function overloading in Java).
;; In this case, power can take either two or three arguments. If it is given two arguments,
;; the code on line 2 is run, which is a recursive call to power, but this time with 3 arguments.
;; This will call the code starting on line 3, in which the symbol current is initialized to 1,
;; and x and y are just passed along as they are.
(defn power
  ([x y] (power x y 1))
  ([x y current]
  (if (= y 0)
    current
    (if (> y 0)
      (power x (- y 1) (* x current))
      (power x (+ y 1) (/ current x))))))
