## Generate flatbuffers transactions

`flatc -j s*.fbs`

Docs: http://google.github.io/flatbuffers/

You can install `flatc` manually

```
git clone https://github.com/google/flatbuffers.git
cd flatbuffers
cmake -G "Unix Makefiles"
make
```
You can use syntax like:

```$xslt
flatc -j *.fbs
```


