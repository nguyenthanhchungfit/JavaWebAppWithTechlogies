namespace java models
include "referencesobj.thrift"

struct Kind{
	1: required string id,
	2: string name,
	3: list<referencesobj.Referencer> songs,
	4: i32 amount_songs
}

struct KindResult{
	1: required i16 result = 0,
	2: optional Kind kind
}
