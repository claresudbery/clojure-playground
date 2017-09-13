

;; DESTRUCTURING:
;; Allows you to bind names to the values inside a data structure.
;; Consider the following example of extracting and naming values in a vector.
;; (See also here: https://clojure.org/guides/destructuring)
(def my-line [[5 10] [10 20]])
(let [p1 (first my-line)
      p2 (second my-line)
      x1 (first p1)
      y1 (second p1)
      x2 (first p2)
      y2 (second p2)]
  (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))
;; Output: "Line from ( 5 , 10 ) to ( 10 , 20 )"
;; Destructuring allows us to make our code cleaner:
;; Using the same vector as above
(let [[p1 p2] my-line
      [x1 y1] p1
      [x2 y2] p2]
 (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))
;; Output is the same: "Line from ( 5 , 10 ) to ( 10 , 20 )"

;; def for the following examples:
(def names ["Michael" "Amber" "Aaron" "Nick" "Earl" "Joe"])

;; You can ignore bindings that you don’t intend on using by binding them to any symbol of your choosing.
;; The convention for this is to use an underscore.
(let [[item1 _ item3 _ item5 _] names]
  (println "Odd names:" item1 item3 item5))
;; Output: Odd names: Michael Aaron Earl

;; You can use :as all to bind the entire vector to the symbol all.
(let [[item1 :as all] names]
  (println "The first name from" all "is" item1))
;; Output: The first name from [Michael Amber Aaron Nick Earl Joe] is Michael

;; we can use & to combine the tail elements into a sequence.
(let [[item1 & remaining] names]
  (println item1)
  (apply println remaining))
;; Output:
;; Michael
;; Amber Aaron Nick Earl Joe

;; You can combine any or all of these techniques at the same time at your discretion.
(def fruits ["apple" "orange" "strawberry" "peach" "pear" "lemon"])
(let [[item1 _ item3 & remaining :as all-fruits] fruits]
  (println "The first and third fruits are" item1 "and" item3)
  (println "These were taken from" all-fruits)
  (println "The fruits after them are" remaining))
;; Output:
;; The first and third fruits are apple and strawberry
;; These were taken from [apple orange strawberry peach pear lemon]
;; The fruits after them are (peach pear lemon)

;; Destructuring can also be nested to get access to arbitrary levels of sequential structure.
(def my-line [[5 10] [10 20]])
(let [[[x1 y1][x2 y2]] my-line]
  (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))
;= "Line from ( 5 , 10 ) to ( 10 , 20 )"

;; ASSOCIATIVE DESTRUCTURING
;; EVERYTHING ABOVE WAS SEQUENTIAL DESTRUCTURING.
;; Associative destructuring is similar to sequential destructuring,
;; but applied instead to associative (key-value) structures (including maps, records, vectors, etc).
(def client {:name "Super Co."
             :location "Philadelphia"
             :description "The worldwide leader in plastic tableware."})
(let [{name :name
       location :location
       description :description} client]
  (println name location "-" description))
;; Output:
;= Super Co. Philadelphia - The worldwide leader in plastic tableware.
;;
;; Also allows you to supply a default value if the key is not present in the associative value with the :or key:
(let [{category :category, :or {category "Category not found"}} client]
  (println category))
;= Category not found
;;
;; You can use :as like you can with sequential destructuring.
;; The :as and :or keywords can be combined.
(def my-map {:a "A" :b "B" :c 3 :d 4})
(let [{a :a, x :x, :or {x "Not found!"}, :as all} my-map]
  (println "I got" a "from" all)
  (println "Where is x?" x))
;= I got A from {:a "A" :b "B" :c 3 :d 4}
;= Where is x? Not found!