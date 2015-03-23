(ns hw1.core)

(defn delete-at
  "delete-at which takes two arguments: a list and an integer and returns the list with the high-level element at that location (zero-based indexing) deleted."
  [list1 index]
  (if (> index (count list1))
    (throw (Exception. "Delete at was called with invalid index " index))
     ((fn worker
     [working-from ctr]
     (if (= ctr index)
       (rest working-from)
       (cons (first working-from) (worker (rest working-from) (+ 1 ctr)))))
      ;; Call lambda with initial args
      list1 0)))

(defn delete-all-at
  "delete-all-at which takes as arguments a list and one or more integers
  and returns the list with the high-level elements at all the locations
  (zero-based indexing) deleted"
  [list1 & indexs]
  (cond (= (count indexs) 0) list1
        (= (count indexs) 1) (delete-at list1 (first indexs))
        (some #(or (> % (count list1)) (< % 0)) indexs)
        (throw (Exception. "You have provided an invalid index"))
        :else
        ((fn worker
           [prev-index indexs-left]
           (if (empty? indexs-left)
             (drop (+ prev-index 1) list1)
             (concat (drop (+ prev-index 1)
                           (drop-last (- (count list1) (first indexs-left))
                                      list1))
                     #_(drop-last (- (count list) (first indexs-left)))
                     (worker (first indexs-left) (rest indexs-left)))))
         -1  (sort indexs))))

(defn similar
  "similar that takes two lists as arguments and returns a list of the high-level elements that are in both lists"
  [list1 list2]
  ((fn worker
     [working-from result]
     (if (empty? working-from)
       (reverse result)
       (if (some #(= (first working-from) %) list2)
         (worker (rest working-from) (cons (first working-from) result))
         (worker (rest working-from) result))))
   list1 '()))

(defn alone
  "alone that takes two lists as arguments and returns a list of the high-level
  elements that are in one or the other but not in both lists."
  [list1 list2]
  (if (> (count list2) (count list1))
    ;; Swap order to make sure elements are compared from the larger list
    (alone list2 list1)
    ((fn worker
       [working-from result]
     (cond (empty? working-from) (reverse result)
           (not (some #(= % (first working-from)) list2)) (worker (rest working-from) (cons (first working-from) result))
           :else (worker (rest working-from) result)))
     list1 '())))

(defn mcons
  "version of cons called mcons that takes any number of arguments.
  The penultimate argument should be consed into the last; the one before
  that should be consed into the resulting value and so on"
  [& args]
  (if (not (or (instance? clojure.lang.PersistentList (last args))
               (instance? clojure.lang.PersistentList$EmptyList (last args))))
   (throw (Exception. "The last argument to mcons must be a list"))
   ((fn worker
     [working-from result]
     (if (empty? working-from)
       result
       (worker (butlast working-from) (cons (last working-from) result))))
   (butlast args) (last args))))

(defn invert
  "invert that takes one parameter, a list with an even-number of elements, and returns the list with each pair of elements swapped"
  [alist]
  (if (not (= (mod (count alist) 2) 0))
    (throw (Exception. "Invert should be called with an even number of elements in the list")))
  ((fn worker
     [working-from]
     (if (= 2 (count working-from))
       (cons (first (rest working-from)) (cons (first working-from) '()))
       (cons (first (rest working-from)) (cons (first working-from) (worker (rest (rest working-from)))))))
   alist))

(def nums-list '(0 1 2 3 4 5 6 7 8 9))
(def nums-list2 '(1 3 5 7 9 11 13 15))
(def sym-list '('1 '2 'a 'test '5 '6))
(def sym-list2 '('a '5 'c '3 '1 '11))

(defn test-delete-at
  []
  (println "\nTesting delete-at \nindex: " 0
           "\nlist: " nums-list
           "\nresult: " (delete-at nums-list 0))

  (println "\nTesting delete-at \nindex: " 4
           "\nlist: " nums-list
           "\nresult: " (delete-at nums-list 4))

  (println "\nTesting delete-at \nindex: " (- (count nums-list) 1)
           "\nlist: " nums-list
           "\nresult: " (delete-at nums-list (- (count nums-list) 1)))

  (println "\nTesting delete-at \nindex: " 0
           "\nlist: " sym-list
           "\nresult: " (delete-at sym-list 0))

  (println "\nTesting delete-at \nindex: " 2
           "\nlist: " sym-list
           "\nresult: " (delete-at sym-list 2))

  (println "\nTesting delete-at \nindex: " (- (count sym-list) 1)
           "\nlist: " sym-list
           "\nresult: " (delete-at sym-list (- (count sym-list) 1)))

  (println "\nTesting delete-at \nindex: " 0
           "\nlist: " (cons nums-list sym-list)
           "\nresult: " (delete-at (cons nums-list sym-list) 0))

  (println "\nTesting delete-at \nindex: " 4
           "\nlist: " (cons nums-list sym-list)
           "\nresult: " (delete-at (cons nums-list sym-list) 0))

  (println "\nTesting delete-at \nindex: " (- (count nums-list) 1)
           "\nlist: " nums-list
           "\nresult: " (delete-at nums-list (- (count nums-list) 1))))

(defn test-delete-all-at
  []
  (println "\nTesting delete-all-at \nindex: " 0
           "\nlist: " nums-list
           "\nresult: " (delete-all-at nums-list 0))

  (println "\nTesting delete-all-at \nindex: " 1 ", " 4 ", " (- (count nums-list) 1)
           "\nlist: " nums-list
           "\nresult: " (delete-all-at nums-list 1 4 (- (count nums-list) 1)))

  (println "\nTesting delete-all-at \nindex: No indexes"
           "\nlist: " nums-list
           "\nresult: " (delete-all-at nums-list))

  (println "\nTesting delete-all-at \nindex: " 0 ", " 1
           "\nlist: " (cons nums-list sym-list)
           "\nresult: " (delete-all-at (cons nums-list sym-list) 0 1))

  (println "\nTesting delete-all-at \nindex: " 1 ", " 4 ", " (- (count (cons nums-list sym-list)) 1)
           "\nlist: " (cons nums-list sym-list)
           "\nresult: " (delete-all-at (cons nums-list sym-list) 1 4 (- (count (cons nums-list sym-list)) 1)))

  (println "\nTesting delete-all-at \nindex: No indexes"
           "\nlist: " (cons nums-list sym-list)
           "\nresult: " (delete-all-at (cons nums-list sym-list))))

(defn test-similar
  []
  (println "\nTesting similar \nlist1 : " nums-list
           "\nlist2: " nums-list2
           "\nresult: " (similar nums-list nums-list2))

  (println "\nTesting similar \nlist1 : " nums-list
           "\nlist2: " nums-list
           "\nresult: " (similar nums-list nums-list))

  (println "\nTesting similar \nlist1 : " sym-list
           "\nlist2: " sym-list2
           "\nresult: " (similar sym-list sym-list2))

  (println "\nTesting similar \nlist1 : " sym-list
           "\nlist2: " sym-list
           "\nresult: " (similar sym-list sym-list)))

(defn test-alone
  []
  (println "\nTesting alone \nlist1 : " nums-list
           "\nlist2: " nums-list2
           "\nresult: " (alone nums-list nums-list2))

  (println "\nTesting alone \nlist1 : " nums-list
           "\nlist2: " nums-list
           "\nresult: " (alone nums-list nums-list))

  (println "\nTesting alone \nlist1 : " sym-list
           "\nlist2: " sym-list2
           "\nresult: " (alone sym-list sym-list2))

  (println "\nTesting alone \nlist1 : " sym-list
           "\nlist2: " sym-list
           "\nresult: " (alone sym-list sym-list)))

(defn test-mcons
  []
  (println "\nTesting mcons \nelements : " nums-list ", " 11 ", " 12 ", " 'a
           "\nlist: " nums-list2
           "\nresult: " (mcons nums-list 11 12 'a nums-list2))

  (println "\nTesting mcons \nelements : " nums-list2 ", " 12 ", " 'b
           "\nlist: " nums-list
           "\nresult: " (mcons nums-list2 12 'b nums-list))

  (println "\nTesting mcons \nelements : " sym-list ", " 5 ", " 'c
           "\nlist2: " sym-list2
           "\nresult: " (mcons sym-list 5 'c sym-list2))

  (println "\nTesting mcons \nelements : " sym-list2 ", " 4 ", " 'd
           "\nlist2: " sym-list
           "\nresult: " (mcons sym-list2 4 'd sym-list)))

(defn test-invert
  []
  (println "\nTesting invert  \nlist : " nums-list
           "\nresult: " (invert nums-list))

  (println "\nTesting invert \nlist : " nums-list2
           "\nresult: " (invert nums-list2))

  (println "\nTesting invert \nlist : " (cons nums-list2 (cons 'o nums-list))
           "\nresult: " (invert (cons nums-list2 (cons 'o nums-list))))

  (println "\nTesting invert \nlist : " sym-list
           "\nresult: " (invert sym-list))

  (println "\nTesting invert \nlist : " sym-list2
           "\nresult: " (invert sym-list2))

  (println "\nTesting invert \nlist : " (cons sym-list2 (cons 'a sym-list))
           "\nresult: " (invert (cons sym-list2 (cons 'a sym-list)))))

(defn -main
  []
  ;; Only valid input tested as there are too many cases to test otherwise
  (test-delete-at)
  (test-delete-all-at)
  (test-similar)
  (test-alone)
  (test-mcons)
  (test-invert))
