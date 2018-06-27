namespace java thrift_services

include "Song.thrift"

service SongServices{
	list<Song.Song> getSongsSearchAPIByName(1: string name),
	Song.SongResult getSongById(1: string id),
	list<Song.Song> getSongsSearchESEByName(1: string name)
}
