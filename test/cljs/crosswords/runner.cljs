(ns crosswords.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [crosswords.core-test]))

(doo-tests 'crosswords.core-test)
