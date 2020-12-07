import sys
import json
import geopandas
from geopandas import GeoSeries
from shapely.ops import unary_union
from shapely.geometry import Polygon, MultiPolygon

districtingId = sys.argv[1]
# districtingId = 314

with open('./src/main/resources/jsons/districtings/{}_precincts_data.json'.format(districtingId), 'r+') as districts_file:
    district_data = json.load(districts_file)
    print(district_data)

if district_data["State"] == "LA":
    precincts_data = geopandas.read_file("./src/main/resources/jsons/precincts/LouisianaPrecincts.json")
elif district_data["State"] == "PA":
    precincts_data = geopandas.read_file("./src/main/resources/jsons/precincts/PennsylvaniaPrecincts.json")
else:
    precincts_data = geopandas.read_file("./src/main/resources/jsons/precincts/CaliforniaPrecincts.json")

average_map_precincts = district_data["Districtings"][0]["Districts"]
extreme_map_precincts = district_data["Districtings"][1]["Districts"]
average_map_id = district_data["Districtings"][0]["DistrictingId"]
extreme_map_id = district_data["Districtings"][1]["DistrictingId"]

average_districts = []
# i=0
for precincts in average_map_precincts:
    precinct_list = precincts['precincts']
    joined = geopandas.GeoSeries()
    for precinct_id in precinct_list:
        precinct = precincts_data.loc[precincts_data['unique_id']==precinct_id]["geometry"]
        joined = joined.geometry.append(precinct)

    district = joined.unary_union
    if not isinstance(district, MultiPolygon):
        district = district.exterior
        district = geopandas.GeoSeries(Polygon(district))
    else:
        district = MultiPolygon(Polygon(p.exterior) for p in district)
        district = geopandas.GeoSeries(district)

    district = district.__geo_interface__
    average_districts.append({"type": "Feature", "geometry": district["features"][0]["geometry"]})

    # with open('./{}.json'.format(i), 'w+') as individual_file:
    #     json.dump({"type":"FeatureCollection", "features":[{"type": "Feature", "geometry": district["features"][0]["geometry"]}]}, individual_file)
    # i+=1

extreme_districts = []
for precincts in extreme_map_precincts:
    precinct_list = precincts['precincts']
    joined = geopandas.GeoSeries()
    for precinct_id in precinct_list:
        precinct = precincts_data.loc[precincts_data['unique_id']==precinct_id]["geometry"]
        joined = joined.geometry.append(precinct)

    district = joined.unary_union
    if not isinstance(district, MultiPolygon):
        district = district.exterior
        district = geopandas.GeoSeries(Polygon(district))
    else:
        district = MultiPolygon(Polygon(p.exterior) for p in district)
        district = geopandas.GeoSeries(district)

    district = district.__geo_interface__
    extreme_districts.append({"type": "Feature", "geometry": district["features"][0]["geometry"]})

data = {"Districtings": [{"DistrictingId": average_map_id, "data":{"type":"FeatureCollection", "features":average_districts}},{"DistrictingId": extreme_map_id, "data":{"type":"FeatureCollection", "features":extreme_districts}}]}
# data = {"type":"FeatureCollection", "features":average_districts}
with open('./src/main/resources/jsons/districtings/{}_districts_data.json'.format(districtingId), 'w+') as outfile:
    json.dump(data, outfile)