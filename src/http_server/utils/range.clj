(ns http-server.utils.range
  (:require [http-server.utils.function :refer :all]))

(defn- full-range [start end]
  (let [s (read-string start)
        e (read-string end)]
    [s e]))

(defn- start-range [start file-length]
  (let [s (read-string start)]
    [s (dec file-length)]))

(defn- end-range [end file-length]
  (let [e (read-string end)]
    [(- file-length e) (dec file-length)]))

(defn calculate-range [range-header file-length]
  (let [[_ start end] (re-matches #"bytes=(\d*)-(\d*)" range-header)]
    (cond
      (and (not-empty? start) (not-empty? end)) (full-range start end)
      (not-empty? start) (start-range start file-length)
      (not-empty? end)   (end-range end file-length))))

(defn valid-range? [from to file-length]
  (not (or
        (> to file-length)
        (> from file-length)
        (< to 0)
        (< from 0))))
