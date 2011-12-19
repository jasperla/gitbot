;  Copyright (c) 2011 Jasper Lievisse Adriaanse <jasper@humppa.nl>
;  The use and distribution terms for this software are covered by the
;  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;  which can be found in the file epl-v10.html at the root of this distribution.
;  By using this software in any fashion, you are agreeing to be bound by
;  the terms of this license.
;  You must not remove this notice, or any other, from this software.

(ns ^{:author "Jasper Lievisse Adriaanse"}
  gitbot.config)

;; Set a few default settings, which can be overriden on the commandline.
(def defaults {:username "gitbot-clj@jabber.org"
               :host "jabber.org"
               :domain "jabber.org"})

;; Define an empty map into which we'll merge user credentials later on.
(def credentials {})

(defn save-credentials
  [auth]
  (def credentials (merge credentials auth)))

(defn get-connection-config
  "Merge the default configuration into a map of cli options (without nil fields)."
  [args]
  (let [config (into {} (filter second args))]
    (merge defaults config)))
