namespace java models
include "referencesobj.thrift"

struct Video{
	1: required string id,
	2: string name,
	3: list<referencesobj.Referencer> singers,
	4: string lyrics,
	5: list<string> qualities,
	6: i64 views,
	7: i16 duration,
	8: string comment,
	9: string image
}

struct VideoResult{
	1: required i16 result = 0,
	2: optional Video video
}
