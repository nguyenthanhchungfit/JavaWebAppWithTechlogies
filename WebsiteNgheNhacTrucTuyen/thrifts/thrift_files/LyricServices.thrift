namespace java thrift_services

include "Lyric.thrift"

service LyricServices{
	Lyric.LyricResult getLyricByIdAndPage(1: string id, 2: string page),
	list<Lyric.DataLyric> getDataLyricsById(1: string id)
}
