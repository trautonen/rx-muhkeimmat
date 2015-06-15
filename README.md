rx-muhkeimmat
=============

Attempt to solve Wunderdog's summer [coding challenge](http://wunderdog.fi/koodaus-pahkina-kesa-2015)
with reactive streams, specifically with RxScala.

The solution tries not to be as efficient as possible, but rather keep the FRP idiom as close as
possible. This is quite hard because of the nature of the challenge. A lot of state must be kept
for analyzing the words.

The stream flow goes as follows:
```
_ Text file
|
 \_ Characters
  |
   \_ Words
    |
     \_ Word pairs
      |
       \_ Word pair groups
        |
         \_ Most unique chars containing group
```

Each phase in the stream flow contains only its own state, no shared state is used to optimize the
performance and the level of abstraction is not leaked. Each phase transforms only one type to
the next one.

