namespace java models

struct DataLyric{
	1: string contributor,
	2: string content
}

struct Lyric{
	1: required string id,
	2: list<DataLyric> datas
}

struct LyricResult{
	1: required i16 result = 0,
	2: optional Lyric lyric
}
