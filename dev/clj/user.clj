(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [crosswords.system :refer [new-system]]))

(reloaded.repl/set-init! #(new-system {}))
