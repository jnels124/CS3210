(ns practice.core)

(defn my-max
 "my-max takes a list of numbers and returns the largest value in the list. If the list is empty, my-max returns -1."
 [nums]
 (if (empty? nums)
   -1
   ((fn find-max
     [high nums-left]
     (if (empty? nums-left)
       high
       (if (> (first nums-left) high)
         (find-max (first nums-left) (rest nums-left))
         (find-max high (rest nums-left)))))
    ;; Call find-max with inital args
    (first nums) (rest nums))))

(defn even-sum
  "even-sum takes a list of numbers and returns the sum of the even values."
  [nums]
  ((fn adder
    [currsum nums-left]
    (if (empty? nums-left)
      currsum
      (adder (+ currsum (first nums-left)) (rest nums-left))))
   ;; Call even sum with additional args
   0 (filter #(== (mod % 2) 0) nums)))

(defn all-pos?
  "all-pos? takes a list of numbers and returns true if all values are positive,
   or if the original list is empty, and false otherwise."
  [nums]
  (if (empty? nums)
    true
    (if (< (first nums) 0)
      false
      (all-pos? (rest nums)))))
