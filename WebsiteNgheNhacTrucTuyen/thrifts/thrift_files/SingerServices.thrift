namespace java thrift_services

include "Singer.thrift"

service SingerServices{
	list<Singer.Singer> getSingersByName(1: string name),
	Singer.SingerResult getSingerById(1: string id)
}
