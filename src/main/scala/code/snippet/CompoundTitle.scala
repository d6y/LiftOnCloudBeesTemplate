package code.snippet

object CompoundTitle {
  def render = <title><lift:Menu.title /> - <lift:loc locid="text.appName"/></title>
}