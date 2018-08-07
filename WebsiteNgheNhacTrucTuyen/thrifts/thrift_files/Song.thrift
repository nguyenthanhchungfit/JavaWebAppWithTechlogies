namespace java models
include "referencesobj.thrift"

struct Song{
	1: required string id,
	2: string name,
	3: referencesobj.Referencer album,
	4: string lyrics,
	5: list<string> composers,
	6: string kara,
	7: i16 duration = 0,
	8: list<referencesobj.Referencer> kinds,
	9: list<referencesobj.Referencer> singers,
	10: i64 views,
	11: string comment,
	12: string image
}

struct SongResult{
	1: required i16 result = 0,
	2: optional Song song
}

