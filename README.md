# StarkHub

Tony Stark, our beloved Avenger is worried about content restrictions being imposed by
YouTube. Being a futurist he is senses a threat, that one day YouTube might have full control
over all the video content. Inspired from BitTorrent, he thought of a Peer to Peer streaming
service with features similar to YouTube. Tony is too busy while making his new bleeding
edge armor. So he needs your help to create this platform while he prepares for a bigger
threat.
PS. The team with best implementation of this software will get Stark Internship ;)

### Basic Features:
- [X] A user account where each user can be content creator or viewer (or both).
- [ ] Content creators can have multiple channels that they can manage.
- [ ] Content creators can add video to any of their channels.
- [ ] Viewers can subscribe to any channel and they must also get notified every time a new video is added to the channel they are subscribed to.
- [ ] Content creators must keep their instance of StarkHub up and always running, so that in case a user requests a video then it must stream it. If the instance of StarkHub is down then all the videos of the respective content creator must be made temporarily unavailable.
- [ ] Viewers can search all the videos available over the network by the video title (Almost similar to youtube search).
- [ ] Viewer can also like, comment and add a video to their watch-later list.
- [ ] A user profile dashboard.
<br>&nbsp;&nbsp;&nbsp;&nbsp; 1. If user is a content creator then he must get stats of all the videos added (Like
number of likes, view count etc.) in his dashboard.
<br>&nbsp;&nbsp;&nbsp;&nbsp; 2. If user is a viewer then he must get log of all his activities. (More the statistics
you show, more the points you get).

### Advanced Features:
- [ ] Content creators can also add tags to video whenever they add video.
- [ ] Viewers can also search videos based on those tags.
- [ ] A trending-videos page, where viewers can see the list of trending videos (For eg. You may set the criteria for trending as the number of unique views a video gets per hour)
- [ ] Two plans for content creators - free and premium.
<br>&nbsp;&nbsp;&nbsp;&nbsp; 1. Free plan - content creator have to keep their instance of StarkHub up and running(if their instance goes down, their content will be immediately unavailable).
<br>&nbsp;&nbsp;&nbsp;&nbsp; 2. Premium plan - a copy of all the content added by the creator(with premium membership) will be stored at one extra peer (who may be a content creator or a content viewer) and in case of downtime of the original creator’s instance, this cache-peer would be used for streaming purposes. Ensure that the cache-peer on which the data is stored as backup cannot modify it. (You can use a checksum for achieving this).
- [ ] Provide an overall rating for every channel based on the overall downtime, number of subscribers and several other factors.
- [ ] Recommend videos to viewers based on the videos they previously watched and the channels they have subscribed. (You are free to explore any recommender system API for this feature)
- [ ] Bonus points for UI that follow Material Design Guidelines.
