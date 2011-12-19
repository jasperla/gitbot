# gitbot

FIXME: write description

## Usage

For quick REPL testing (until it's capable of daemonizing):

    gitbot.core=> (def options {:host "jabber.org", :domain "jabber.org",
        :username "gitbot-clj@jabber.org", :password "SECRET"})
    #'gitbot.core/options
    gitbot.core=> (start options)  
    #'gitbot.core/gitbot
    gitbot.core=> 

At this point the bot is running and can be interacted with.

## License

Copyright (C) 2011 Jasper Lievisse Adriaanse <jasper@humppa.nl>

Distributed under the Eclipse Public License, the same as Clojure
(see the file COPYING).
