# Artemis large message cutoff

Example project that shows that Artemis cuts off large bytes messages with compression that slightly exceed the minimal large message size.

# Conditions to reproduce

- Message size must exceed the minimal large message size
- Message size must be less than twice the minimal large message size
- Minimal large message size must be more than about 1MiB
- Large message compression must be enabled
- Message must be a JMS bytes message


- If messages are smaller than the minimal large message size there is no issue
- If messages are twice as big as the minimal large message size there is no issue
- If the minimal large message size is less than about 1MiB there is no issue
- If large messages are not compressed there is no issue
- If the message is a text message there is no issue

Tested with both Artemis client version 2.18.0 and 2.20.0 while my Artemis server is at 2.20.0
