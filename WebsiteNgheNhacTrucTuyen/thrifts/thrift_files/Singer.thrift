namespace java models
include "referencesobj.thrift"

struct Singer{
	1: required string id,
	2: string name,
	3: string realname,
	4: string dob,
	5: string country,
	6: string description,
	7: list<referencesobj.Referencer> songs,
	8: list<referencesobj.Referencer> albums,
	9: list<referencesobj.Referencer> videos,
	10: string imgCover,
	11: string imgAvatar 			 		
}

struct SingerResult{
	1: required i16 result = 0,
	2: optional Singer singer
}


