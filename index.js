'use strict';

let React = require('react-native');


let {
  View,
  Component,
  requireNativeComponent,
  PropTypes,
  DeviceEventEmitter,
  Platform,
  Image,
  StyleSheet,
  ScrollView,
  ScrollResponder
} = React;
var REBOUNDSCROLLVIEW = 'ReboundScrollView';
var INNERVIEW = 'InnerScrollView';
/* RNGMAPS COMP */
var boundScrollView = {
  name: 'ReboundScrollView',
  propTypes: {
	  scaleX:PropTypes.number,
	  scaleY:PropTypes.number,
	  translateY:PropTypes.number,
	  translateX:PropTypes.number,
	  rotation:PropTypes.number,
  },
};
let RCTBoundScrollView
if (Platform.OS === 'android') {
	RCTBoundScrollView = requireNativeComponent('ReboundScrollView', boundScrollView);
}
var styles = StyleSheet.create({
  base: {
    flex: 1,
  },
  contentContainerHorizontal: {
    alignSelf: 'flex-start',
    flexDirection: 'row',
  },
});

class BoundScrollView extends ScrollView {
  mixins: [ScrollResponder.Mixin]
  constructor (props) {
    super(props);
	 this.scrollResponderMixinGetInitialState();
  }

  componentDidMount () {

  }
  setNativeProps (props: Object) {
    this.refs[REBOUNDSCROLLVIEW].setNativeProps(props);
  }

  /**
   * Returns a reference to the underlying scroll responder, which supports
   * operations like `scrollTo`. All ScrollView-like components should
   * implement this method so that they can be composed while providing access
   * to the underlying scroll responder's methods.
   */
  getScrollResponder (): ReactComponent {
    return this;
  }

  getInnerViewNode (): any {
    return React.findNodeHandle(this.refs[INNERVIEW]);
  }

  scrollTo (destY?: number, destX?: number) {
    // $FlowFixMe - Don't know how to pass Mixin correctly. Postpone for now
    this.getScrollResponder().scrollResponderScrollTo(destX || 0, destY || 0);
  }

  scrollWithoutAnimationTo (destY?: number, destX?: number) {
    // $FlowFixMe - Don't know how to pass Mixin correctly. Postpone for now
    this.getScrollResponder().scrollResponderScrollWithouthAnimationTo(
      destX || 0,
      destY || 0,
    );
  }

  handleScroll (e: Event) {
    if (__DEV__) {
      if (this.props.onScroll && !this.props.scrollEventThrottle) {
        console.log(
          'You specified `onScroll` on a <ScrollView> but not ' +
          '`scrollEventThrottle`. You will only receive one event. ' +
          'Using `16` you get all the events but be aware that it may ' +
          'cause frame drops, use a bigger number if you don\'t need as ' +
          'much precision.'
        );
      }
    }
    if (Platform.OS === 'android') {
      if (this.props.keyboardDismissMode === 'on-drag') {
        dismissKeyboard();
      }
    }
    this.scrollResponderHandleScroll(e);
  }
  
  render () {
     var props = {
         style: ([styles.base, this.props.style]: ?Array<any>),
         onTouchStart: this.scrollResponderHandleTouchStart,
         onTouchMove: this.scrollResponderHandleTouchMove,
         onTouchEnd: this.scrollResponderHandleTouchEnd,
         onScrollBeginDrag: this.scrollResponderHandleScrollBeginDrag,
         onScrollEndDrag: this.scrollResponderHandleScrollEndDrag,
         onMomentumScrollBegin: this.scrollResponderHandleMomentumScrollBegin,
         onMomentumScrollEnd: this.scrollResponderHandleMomentumScrollEnd,
         onStartShouldSetResponder: this.scrollResponderHandleStartShouldSetResponder,
         onStartShouldSetResponderCapture: this.scrollResponderHandleStartShouldSetResponderCapture,
         onScrollShouldSetResponder: this.scrollResponderHandleScrollShouldSetResponder,
         onScroll: this.handleScroll,
         onResponderGrant: this.scrollResponderHandleResponderGrant,
         onResponderTerminationRequest: this.scrollResponderHandleTerminationRequest,
         onResponderTerminate: this.scrollResponderHandleTerminate,
         onResponderRelease: this.scrollResponderHandleResponderRelease,
         onResponderReject: this.scrollResponderHandleResponderReject,
     }

	 var contentContainer =
    <View
      ref={INNERVIEW}
      removeClippedSubviews={this.props.removeClippedSubviews}
      style={this.props.contentContainerStyle}
      >
      {this.props.children}
    </View>;
    return (<RCTBoundScrollView {...props} ref={REBOUNDSCROLLVIEW}>
          {contentContainer}
        </RCTBoundScrollView>);
  }
}

var styles = StyleSheet.create({
  base: {
    flex: 1,
  },
  contentContainerHorizontal: {
    alignSelf: 'flex-start',
    flexDirection: 'row',
  },
});

 if (Platform.OS === 'ios') {
	BoundScrollView = ScrollView
 }

module.exports = BoundScrollView;
