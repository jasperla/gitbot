(ns gitbot.config)

;; Set a few default settings, which can be overriden on the commandline.
(def defaults {:username "gitbot-clj@jabber.org"
               :host "jabber.org"
               :domain "jabber.org"})

(defn get-connection-config
  "Merge the default configuration into a map of cli options (without nil fields)."
  [args]
  (let [config (into {} (filter second args))]
    (merge defaults config)))
