# Artemis large message cutoff

Example project that shows that Artemis cuts off large bytes messages with compression that slightly exceed the minimal large message size.

Message must exceed the minimal large message size, but not more than twice that value, while using JMS bytes message and using large message compression.

- If messages are smaller than the minimal large message size there is no issue
- If messages are twice as big as the minimal large message size there is no issue
- If large messages are not compressed there is no issue
- If the message is a text message there is no issue

Tested with both Artemis client version 2.18.0 and 2.20.0 while my Artemis server is at 2.20.0