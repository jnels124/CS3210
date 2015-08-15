(ns practice.core-test
  (:require [clojure.test :refer :all]
            [practice.core :refer :all]))

(deftest my-max-empty-list-test
  (testing "my-max with empty list"
    (is (= -1 (my-max '())))))

(deftest my-max-single-entry-tests
  (testing "my-max with 1 item in the list"
    (is (= -1 (my-max '(-1))))
    (is (= 0 (my-max '(0))))
    (is (= 1 (my-max '(1))))))

(deftest my-max-negatives-and-zero-only-test
  (testing "my-max with negatives and zero only in the list"
    (is (= 0 (my-max '(-1 -2 -3 -4 -5 -6 -7 0))))
    (is (= 0 (my-max '(0 -1 -2 -3 -4 -5 -6 -7))))
    (is (= 0 (my-max '(-1 -2 -3 -4 0 -5 -6 -7))))
    (is (= -1 (my-max '(-1 -2 -3 -4 -5 -6 -7))))))
