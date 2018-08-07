namespace java models
include "referencesobj.thrift"

struct Album{
	1: required string id,
	2: string name,
	3: list<referencesobj.Referencer> songs,
	4: string image
}

struct AlbumResult{
	1: required i16 result = 0,
	2: optional Album album
}
