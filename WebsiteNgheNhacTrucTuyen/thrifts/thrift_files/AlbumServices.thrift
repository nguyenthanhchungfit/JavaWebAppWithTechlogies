namespace java thrift_services

include "Album.thrift"

service AlbumServices{
	Album.AlbumResult getAlbumById(1: string id),
	i64 getTotalNumberAlbums()
}
