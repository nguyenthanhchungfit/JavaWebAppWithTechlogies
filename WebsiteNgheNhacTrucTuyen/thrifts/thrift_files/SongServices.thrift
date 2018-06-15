namespace java thrift_services

include "Song.thrift"

service SongServices{
	list<Song.Song> getSongsByName(1: string name),
	Song.SongResult getSongById(1: string id)
}
